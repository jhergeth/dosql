dosql
=====

Execute SQL commands on databases. Specially for transformation of .csv-files with hsql.

Initially I had the task to summarize information from several .csv files. The data initially was exported from a database to which I had no access. So I had to extract the data via csv and work with that. Importing it into a full blown SQL-server and working with that seemed too much effort. Especially as I wanted a tool which could be handled by non IT people.
I ionitially started with Excel, imported and tried to accomplish my goal. 8 hours later I had a fairly complex EXCEL file (NOT suitable for IT amateurs) which solved 90% of the task and was complex and even for the IT guy difficult to expand/adapt to new issues.
So I decided to search for a tool which could run SQL commands on CSV files. Results should again be exported in CSV files.
ETL was the key word! But those tools are again quite complex and it is difficult, at least for the free ones, to generate a simple executable which could be used by the amateur.

Then I read about in memory databases and HSQL. So I decided to implement a simple tool by myself. I used Groovy becaus it seemd the fastest way to get results.
