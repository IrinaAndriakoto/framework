<%
    String nom = (String) request.getAttribute("nom");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h2>\Récupération des données :</h2>
    <p>Bienvenue, <%= nom %></p>
</body>
</html>
