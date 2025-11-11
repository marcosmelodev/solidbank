def menu_principal():
    """Exibe o menu principal"""
    print("\n" + "="*50)
    print("SISTEMA BANCÁRIO".center(50))
    print("="*50)
    print("1. Criar novo cliente")
    print("2. Criar conta corrente")
    print("3. Depositar")
    print("4. Sacar")
    print("5. Extrato")
    print("0. Sair")
    print("="*50)
    return input("Escolha uma opção: ")


def main():
    """Função principal da aplicação"""
    # Inicializar banco de dados
    db_connection = DatabaseConnection()
    db_manager = DatabaseManager(db_connection)
    db_manager.criar_tabelas()
    
    # Inicializar serviço
    banco_service = BancoService(DatabaseConnection())
    
    print("\n🏦 Bem-vindo ao Sistema Bancário!")
    
    while True:
        opcao = menu_principal()
        
        if opcao == "1":
            print("\n--- CRIAR NOVO CLIENTE ---")
            nome = input("Nome: ")
            cpf = input("CPF: ")
            data_nasc = input("Data de nascimento (YYYY-MM-DD): ")
            endereco = input("Endereço: ")
            
            banco_service.criar_cliente(nome, cpf, data_nasc, endereco)
        
        elif opcao == "2":
            print("\n--- CRIAR CONTA CORRENTE ---")
            cpf = input("CPF do cliente: ")
            cliente = banco_service.cliente_repo.buscar_por_cpf(cpf)
            
            if not cliente:
                print("❌ Cliente não encontrado!")
                continue
            
            numero = int(input("Número da conta: "))
            banco_service.criar_conta_corrente(cliente, numero)
        
        elif opcao == "3":
            print("\n--- DEPOSITAR ---")
            numero = int(input("Número da conta: "))
            valor = float(input("Valor do depósito: R$ "))
            
            conta = banco_service.conta_repo.buscar_por_numero(numero)
            if conta:
                deposito = Deposito(valor)
                banco_service.realizar_transacao(conta, deposito)
            else:
                print("❌ Conta não encontrada!")
        
        elif opcao == "4":
            print("\n--- SACAR ---")
            numero = int(input("Número da conta: "))
            valor = float(input("Valor do saque: R$ "))
            
            conta = banco_service.conta_repo.buscar_por_numero(numero)
            if conta:
                saque = Saque(valor)
                banco_service.realizar_transacao(conta, saque)
            else:
                print("❌ Conta não encontrada!")
        
        elif opcao == "5":
            print("\n--- EXTRATO ---")
            numero = int(input("Número da conta: "))
            banco_service.exibir_extrato(numero)
        
        elif opcao == "0":
            print("\n👋 Obrigado por usar nosso sistema!")
            break
        
        else:
            print("❌ Opção inválida!")


if __name__ == "__main__":
    main()