class ContaCorrente(Conta):
    """Conta corrente com limite de saques"""
    
    def __init__(self, cliente: Cliente, limite_saques: int = 3, **kwargs):
        super().__init__(cliente, **kwargs)
        self.limite_saques = limite_saques
        self.saques_realizados = 0
    
    def get_tipo(self) -> str:
        return "Conta Corrente"
    
    def sacar(self, valor: float) -> bool:
        if self.saques_realizados >= self.limite_saques:
            print(f"❌ Limite de {self.limite_saques} saques diários atingido!")
            return False
        
        if super().sacar(valor):
            self.saques_realizados += 1
            return True
        return False