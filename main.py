from utils.menu import exibir_menu

print("\\n🏦 Bem-vindo ao Banco Digital!")
    
while True:
        opcao = exibir_menu()
        
        if opcao == "1":
            nome = input("\\nNome do cliente: ")
            cpf = input("CPF (apenas números): ")
            banco.criar_cliente(nome, cpf)
        
        elif opcao == "2":
            cpf = input("\\nCPF do cliente: ")
            print("\\nTipo de conta:")
            print("[1] Conta Corrente")
            print("[2] Conta Poupança")
            tipo = input("Escolha: ")
            banco.criar_conta(cpf, tipo)
        
        elif opcao == "3":
            numero = int(input("\\nNúmero da conta: "))
            conta = banco.conta_repo.buscar_por_numero(numero)
            if conta:
                valor = float(input("Valor do depósito: R$ "))
                banco.realizar_transacao(conta, "DEPOSITO", valor)
            else:
                print("❌ Conta não encontrada!")
        
        elif opcao == "4":
            numero = int(input("\\nNúmero da conta: "))
            conta = banco.conta_repo.buscar_por_numero(numero)
            if conta:
                valor = float(input("Valor do saque: R$ "))
                banco.realizar_transacao(conta, "SAQUE", valor)
            else:
                print("❌ Conta não encontrada!")
        
        elif opcao == "5":
            numero = int(input("\\nNúmero da conta: "))
            conta = banco.conta_repo.buscar_por_numero(numero)
            if conta:
                banco.carregar_extrato(conta)
            else:
                print("❌ Conta não encontrada!")
        
        elif opcao == "6":
            cpf = input("\\nCPF do cliente: ")
            cliente = banco.cliente_repo.buscar_por_cpf(cpf)
            if cliente:
                contas = banco.conta_repo.listar_contas_cliente(cliente.id)
                print(f"\\n📋 Contas de {cliente.nome}:")
                for conta in contas:
                    print(f"  • {conta.get_tipo()} - Conta: {conta.numero} - Saldo: R$ {conta.saldo:.2f}")
            else:
                print("❌ Cliente não encontrado!")
        
        elif opcao == "0":
            print("\\n👋 Obrigado por usar o Banco Digital!")
            break
        
        else:
            print("\\n❌ Opção inválida!")

if __name__ == "__main__":
    main()