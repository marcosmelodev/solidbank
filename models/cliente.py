
class Cliente:
    
 def __init__(self, pessoa: PessoaFisica, endereco: str):
        self.id: Optional[int] = None
        self.nome = pessoa.nome
        self.cpf = pessoa.cpf
        self.data_nascimento = pessoa.data_nascimento
        self.endereco = endereco
        self.contas: List['Conta'] = []
    
def adicionar_conta(self, conta):
        """Adiciona uma conta ao cliente"""
        self.contas.append(conta)
