# MutantXRepository
Repositorio prueba Mercado Libre
------------------------------------------------------------------------------------------------------------------------
Especificaciones Proyecto Mutant
------------------------------------------------------------------------------
-Proyecto implementado en Java con maven, jersey Rest, Junit, mockito.

-Para almacenar datos se utilizo una Base de datos en memoria(H2)
------------------------------------------------------------------------------
Paso para consumir la API de Mutant:
------------------------------------------------------------------------------------------------------------------------
Operaciones:

1.) Consultar mutante:
------------------------------------------------------------------------------------------------------------------------
*Verbo: POST
*URL: {servidor:port}/mutantservice/mutant
*Request: Recibe un body tipo de dato JSON:

Ejemplo: 
		{
		"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
		}

Respuestas:

Estado Http 200 ok
Mensaje: 
Un objeto JSON con la estructura:
{
    "message": "La sequencia de ADN pertenece a un mutante  ID:  1"
}

Importante: el ID retornado es el UID que identifica el recurso mutante para su posterior busqueda de estadísticas


Estado Http 403
Mensaje:
Un objeto JSON con la estructura:
{
    "message": "La sequencia de ADN no pertenece a un mutante"
}

Estado Http 500
Mensaje:
Un objeto JSON con la estructura:
{
    "message": "Error"
}

-------------------------------------------------------------------------------------------------------------------------
2.) Verificar estadísticas
------------------------------------------------------------------------------------------------------------------------- 
*Verbo: GET
*URL: {servidor:port}/mutantservice/stats/{id}
*Request: Recibe parametro de tipo entero(UID que identifica el recurso mutante)

Respuestas:

Estado Http 200 ok
Mensaje: 
Un objeto JSON con la estructura:
{
  "message": "count_mutant_dna:3, count_human_dna:33, ratio:9.0"
}


Estado Http 204
Mensaje:
Un objeto JSON con la estructura:
{
    "message": ""
}

Estado Http 500

Mensaje:
Un objeto JSON con la estructura:
{
    "message": "Error"
}

-----------------------------------------------------------------------------------------------------------------------------
by Juan Camilo Quintero 