dosql
=====

Execute SQL commands on csv-files.

Initially I had the task to summarize information from several .csv files. I got the data in csv files.
Importing it into a full blown SQL-server and working with that, seemed too much effort. Especially as I wanted a tool which could be handled by non IT people.

I started with Excel, imported and tried to accomplish my goal. 8 hours later I had a fairly complex EXCEL file (NOT suitable for IT amateurs) which solved 90% of the task and was complex and even for the IT guy difficult to expand/adapt to new issues.

So I decided to search for a tool which could run SQL commands on CSV files. Results should again be exported in CSV files.

Then I read about in memory databases and HSQL. So I decided to implement a simple tool by myself. I used Groovy because it seemd the fastest way to get results.
