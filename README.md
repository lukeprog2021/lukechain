# Meu blockchain em Java
<p>Neste projeto eu utilizei do conceito de blockchain (que eu chamo de LukeChain) e mineração para simular 
um sistema real e entender o sistema de uma forma mais prática de como são os famosos bitcoins e outras cryptomoedas.</p>
<p>Eu optei por aplicar na linguagem Java e POO para criar os algoritimos necessários para aplicação</p>

<h3>Arquitetura do projeto</h3>
<p>O projeto é dividido da seguinte forma:</p>

<ul>
  <li>Block, o bloco que armazena um Hash, (assinatura) a "digital" única de cada bloco, e que armazena dados de 
  transações que serão feitas, de saídas e entradas das moedas. O Hash de cada bloco é calculado a partir do Hash do bloco anterior, assim, se houver alguma alteração em algum bloco
  toda a cadeia de blocos é invalidada, garantindo assim a segurança e criptografia autêntica dos blocos.</li>
  <li>Wallet, a carteira, é onde se recebe as moedas digitais, cada transação feita é registrada em um bloco. A carteira possui duas propriedades importantes como 
  a Chave pública, usada em transações para identificar a carteira para enviar ou receber moedas. A outra propriedade é a Chave Privada, que é usada 
  para assinar e autenticar as transações.</li>
  <li>Transactions, as transações são registradas com uma assinatura única da transação que é calculada a partir das chaves públicas e privadas, além de serem autenticadas por diversos métodos( consulte o código)
  . As transações, se autenticadas como válidas, são registradas em um bloco, caso não, elas são descartadas. Existem dois tipos, Output/Input, que são usadas para atualizar 
  o saldo nas carteiras</li>
  <li>Mine, a mineração é feita a partir da resolução de casos matemáticos complexos, que a resolução através do poder computacional do minerador, gera blocos e grava transações feitas
  dentro deles ( de forma bem superficial, apenas para simplificar)</li>
  <li>Validação, na blockchain, toda modificação, introdução e consulta é verificada e validada, através dos Hash que são gerados de forma unica e interrelacionados, assim aumentando
  a complexidade deles, e criando uma dependencia, aonde que qualquer alteração não autenticada, inválida todo o processo e não autoriza a realização</li>
</ul>

<p>Meu código é apenas uma exemplificação simples do que acontece no mundo real, mas fiel ao conceito e aplicação. Eu achei muito interessante, estou aprendendo sobre e espero ter colaborado
com algo. Sugestões e correções são bem vindas</p>

