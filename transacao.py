class Transacao(ABC):
    """Interface para transações bancárias"""
    
    @property
    @abstractmethod
    def valor(self) -> float:
        pass
    
    @abstractmethod
    def registrar(self, conta: Conta):
        pass
