
class Conta:
    def __init__(self, cliente: Cliente, numero: int, agencia: str = "0001"):
        self.id: Optional[int] = None
        self.numero = numero
        self.agencia = agencia
        self.saldo: float = 0.0
        self.cliente = cliente
        self.historico = Historico()
    
    def saldo_atual(self) -> float:
        """Retorna o saldo atual da conta"""
        return self.saldo
    
    @abstractmethod
    def sacar(self, valor: float) -> bool:
        """Método abstrato para saque (deve ser implementado pelas subclasses)"""
        pass
    
    def depositar(self, valor: float) -> bool:
        """Realiza um depósito na conta"""
        if valor <= 0:
            print("❌ Valor do depósito deve ser positivo!")
            return False
        
        self.saldo += valor
        print(f"✅ Depósito de R$ {valor:.2f} realizado com sucesso!")
        return True
    
    def nova_conta(self, cliente: Cliente, numero: int) -> 'Conta':
        """Factory method para criar nova conta"""
        return type(self)(cliente, numero, self.agencia)
