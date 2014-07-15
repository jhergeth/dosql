/**
 * Created by joachim on 28.06.2014.
 */
import groovy.sql.Sql

import java.nio.file.FileSystemException
import java.sql.SQLException

myLog = false;
myDb = null

Sql rDB() {
    if (myDb == null) {
        myDb = Sql.newInstance("jdbc:hsqldb:file:testdb", 'sa', '', 'org.hsqldb.jdbc.JDBCDriver')
    }
    return myDb
}

void sDB(def path, def con, def usr, def pw, def cls) {
    try {
        this.class.classLoader.rootLoader.addURL(new URL("file:///${path}"))
        myDb = Sql.newInstance(con, usr, pw, cls)
    }
    catch (Exception e) {
        println("Cannot connect to database.Error: ${e.message}")
    }
}

void log(str) {
    if (myLog)
        println(str)
}


void doit(it) {
    if (it.name() == 'exc') {
        if (it.@file == null) {
            println("exc-command without file attribute!")
            return
        }
        log("exc with file <${it.@file}>")
        def doparse = new XmlParser().parse(new File(it.@file))
        doparse.children().each() { itt ->
            doit(itt)
        }
        log("exc finished processing file <${it.@file}>")
    } else if (it.name() == 'opt') {
        myLog = it.@log == 'true' || it.@log == '1'
        log("Set log to  <${myLog}>")
    } else if (it.name() == 'open') {
        String slib = it.text()
        slib = slib.trim()
        slib = slib.replace('\\', '/')
        sDB(slib, it.@con, it.@usr, it.@pas, it.@drv)
        log("Connect using <${slib}>")
    } else if (it.name() == 'sql') {
        log("sql with <${it.text()}>")
        try {
            rDB().execute(it.text())
        }
        catch (SQLException e) {
            println("Error in SQL-statement:${e.message}")
            return
        }
    } else if (it.name() == 'out') {
        log("out to file <${it.@file}> with <${it.text()}>")
        Integer cnt = 0
        def sFile = it.@file
        if (sFile == null) {
            def cols = (it.@cols).split(',')
            try {
                rDB().eachRow(it.text()) { row ->
                    cols.each { c ->
                        print("${row[c].toString().trim()},")
                    }
                    println("")
                    cnt++
                }
            }
            catch (SQLException e) {
                println("Error in SQL-statement:${e.message}")
                return
            }
        } else {
            try {
                def outFile = new File(sFile)
                def wtr = outFile.newWriter()
                def cols = (it.@cols).split(',')
                rDB().eachRow(it.text()) { row ->
                    cols.each { c ->
                        wtr.write("${row[c].toString().trim()},")
                    }
                    wtr.writeLine("")
                    cnt++
                }
                wtr.close()
            } catch (FileSystemException e) {
                println("Cant use file ${sFile}.")
                exit()
            } catch (SQLException e) {
                println("Error in SQL-statement:${e.message}")
                return
            }
        }
        log("${cnt} lines written")
    }
}

def cli = new CliBuilder(usage: "dohsql [script.xml]")
cli.h(longOpt: 'help', 'usage information:\ndohsql starts a hsql database and executes the commands in <script.xml> on it.\nIf no filename is give as a parameter, "dohsql.xml" is used.')
def opt = cli.parse(args)
if (!opt) return

if (opt.h) cli.usage()

def xmlFile = "dohsql.xml"
if (opt.arguments().size() == 1) {
    xmlFile = opt.arguments()[0]
}

def dohsql = null
try {
    dohsql = new XmlParser().parse(new File(xmlFile))
}
catch (FileNotFoundException e) {
    println("Error accessing file ${xmlFile}")
    return
}


dohsql.children().each() { it ->
    doit(it)
}
