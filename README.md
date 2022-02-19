Segunda parte do trabalho de implementação da disciplina de Teoria dos Grafos 
(2021.2), feito por Rayssa Almeida (@rayrainbow-sys) e, ainda, se aproveitando das 
importantissimas contribuições de Eduarda Ferreira (@ferreira-mev), que está embarcada 
em outras aventuras nessa metade do semestre. 

A biblioteca solicitada, para representação e manipulação de grafos, é o 
pacote `graphs`, atualmente composto apenas pela classe `Graph`, disponível 
no diretório `src/main/graphs`. Esse pacote é tudo que precisa ser importado 
pelo usuário final; a documentação da classe e seus métodos pode ser 
acessada, no formato de página Javadoc, clonando ou baixando este 
repositório e navegando a partir do arquivo `doc/index.html`, disponível neste repositório.

O programa principal (cuja fonte é `src/main/casestudies/Main.java`) foi 
usado por nós para executar os estudos de caso requeridos com os grafos 
fornecidos no [site da disciplina](https://www.cos.ufrj.br/~daniel/grafos/) 
e registrar os resultados num arquivo csv. Esses dados foram incluídos no 
relatório.

O diretório `src/test` contém testes unitários do JUnit 5 para os métodos da 
classe `Graph`, que usamos para pautar o desenvolvimento dela a partir dos 
resultados esperados para grafos simples, conferíveis manualmente. A 
implementação atual passa em todos os testes incluídos.
