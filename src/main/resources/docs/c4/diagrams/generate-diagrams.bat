@echo off
echo Gerando diagramas C4...

java -jar plantuml.jar diagrams/1-contexto.puml -tpng
java -jar plantuml.jar diagrams/2-container.puml -tpng
java -jar plantuml.jar diagrams/3-componente.puml -tpng
java -jar plantuml.jar diagrams/4-codigo.puml -tpng
java -jar plantuml.jar diagrams/sequencia-crud.puml -tpng

echo Diagramas gerados com sucesso!
pause