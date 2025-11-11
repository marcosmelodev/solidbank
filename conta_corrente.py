class ContaCorrente(Conta):
    """Conta corrente com limite e limite de saques"""
    
    def __init__(self, cliente: Cliente, numero: int, agencia: str = "0001",
                 limite: float = 500.0, limite_saques: int = 3):
        super().__init__(cliente, numero, agencia)
        self.limite = limite
        self.limite_saques = limite_saques
        self.saques_realizados = 0
    
    def sacar(self, valor: float) -> bool:
        """Realiza saque com validações específicas da conta corrente"""
        if valor <= 0:
            print("❌ Valor do saque deve ser positivo!")
            return False
        
        if self.saques_realizados >= self.limite_saques:
            print(f"❌ Limite de {self.limite_saques} saques diários atingido!")
            return False
        
        if valor > self.limite:
            print(f"❌ Valor do saque excede o limite de R$ {self.limite:.2f}!")
            return False
        
        if valor > self.saldo:
            print("❌ Saldo insuficiente!")
            return False
        
        self.saldo -= valor
        self.saques_realizados += 1
        print(f"✅ Saque de R$ {valor:.2f} realizado com sucesso!")
        return True