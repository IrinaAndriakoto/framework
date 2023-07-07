<%@ page import="Emp" %>
<%@ page import="etu1924.framework.FileUpload" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Upload</title>
</head>
<body>
    <h2>FileUpload</h2>
    <p>FileName: <%= emp.getFile().getName() %></p>
</body>
</html>