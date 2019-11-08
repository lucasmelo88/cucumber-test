Feature: Consultar endereço de correntista
  Scenario: Consulta de correntista realizada com sucesso
    Given Luis deseja consultar o endereço de um correntista
    When Luis define os parametros da consulta
      | cpf         | accountType |
      | 12123123077 | PJ          |
      | 32128485980 | PF          |
    And realiza sua consulta
    Then o retorno da requisição deverá ser "200"
    And o retorno da requisição deve conter um json com os seguintes dados
      | zipCode  | street          | number | complement |
      | 13035888 | Rua das flores  | 123    | ap.55      |
      | 13055871 | Rua das plantas | 321    |            |
    
  Scenario: Consulta correntista não realizada pois o correntista não consta na base de dados
  Scenario: Consulta de correntista não realizada pois os parâmetros obrigatórios não foram informados
  Scenario: Consulta de correntista não realizada pois parâmetro cpf excedeu o limite de valores
  Scenario: Consulta de correntista não realizada pois parâmetro accountType é inválido