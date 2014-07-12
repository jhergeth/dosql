/**
 * Created by joachim on 26.06.2014.
 */


import groovy.io.GroovyPrintWriter
import groovy.sql.Sql

//create the table definition to insert
String tableName = 'std'
String STD_FILE_NAME = 'ExportAktuellerUnterricht.TXT'
String BER_FILE_NAME = 'KlassenBereiche.TXT'

String dropSTD = """DROP TABLE IF EXISTS std"""
String tableSTD = """CREATE TEXT TABLE std (
    lehrer VARCHAR(16),
    jahr NUMERIC,
    monat NUMERIC,
    tag NUMERIC,
    stunde NUMERIC,
    fach VARCHAR(16),
    klasse VARCHAR(16),
    raum VARCHAR(16),
    i1 NUMERIC,
    i2 NUMERIC,
    i3 NUMERIC,
    i4 NUMERIC,
    dauer NUMERIC,
    i5 NUMERIC
);"""

String dropBER = """DROP TABLE IF EXISTS ber"""
String tableBER = """CREATE TEXT TABLE ber (
    klasse VARCHAR(16),
    bereich VARCHAR(16)
);"""

String sqlCMD = """SELECT bereich, lehrer, SUM(dauer)/45 AS sdauer
    FROM (
        SELECT bereich, klasse, lehrer, dauer
        FROM std
            JOIN ber ON std.klasse = ber.klasse
        ORDER BY bereich, klasse, lehrer
    )
    GROUP BY bereich, lehrer
    ORDER BY bereich, lehrer
"""

//create a new file database and a table corresponding to the csv file
Sql sql = Sql.newInstance("jdbc:hsqldb:file:testdb", 'sa', '', 'org.hsqldb.jdbc.JDBCDriver')
sql.execute(dropSTD)
sql.execute(tableSTD)
sql.execute(dropBER)
sql.execute(tableBER)

//set the source to the csv file
sql.execute("SET TABLE std SOURCE '${STD_FILE_NAME};all_quoted=true'".toString())
sql.execute("SET TABLE ber SOURCE '${BER_FILE_NAME};all_quoted=true'".toString())


def outFile = new File('BereichLehrerStunden.txt')
def wtr = outFile.newWriter()

sql.eachRow(sqlCMD) { row ->
    wtr.writeLine("${row.bereich},${row.lehrer},${row.sdauer}")
}
wtr.close()
