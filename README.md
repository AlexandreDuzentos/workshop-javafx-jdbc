---

# 🛠️ Workshop JavaFX + JDBC – CRUD de Departments e Sellers  

Este repositório contém um projeto desenvolvido no curso **Java COMPLETO Programação Orientada a Objetos + Projetos**, ministrado por **Leonardo Moura Leitão** na **Udemy**. O projeto é um sistema **CRUD (Create, Read, Update, Delete)** para gerenciamento de **Departments** e **Sellers**, utilizando **JavaFX** para a interface gráfica e **JDBC** para a conexão com o banco de dados.  

## 📌 Sobre o Projeto  

O projeto consiste em uma aplicação de desktop que permite:  

✅ **Cadastrar** novos departamentos e vendedores  
✅ **Listar** os registros existentes  
✅ **Atualizar** informações dos registros  
✅ **Excluir** registros do banco de dados  

📺 **Curso na Udemy**:  
🔗 [Acesse o curso oficial](https://www.udemy.com/course/java-curso-completo/)  

---

## 📂 Estrutura do Projeto  

```
📦 workshop-javafx-jdbc
├── 📂 src
│    ├── 📁 application          # Classe principal e inicialização
│    ├── 📁 db                   # Conexão e tratamento de banco de dados
│    ├── 📁 gui                  # Controladores JavaFX (UI)
│    ├── 📁 model
│    │      ├── 📁 entities      # Classes de entidade (Department, Seller)
│    │      ├── 📁 dao           # Interfaces de acesso a dados
│    │      └── 📁 dao.impl      # Implementações JDBC
│    └── module-info.java        # Módulo do JavaFX
└── 📜 README.md                 # Documentação do projeto
```

---

## 🧰 Tecnologias Utilizadas  

- **Java SE** (versão 11 ou superior)  
- **JavaFX** (Interface gráfica)  
- **JDBC** (Conexão com banco de dados)  
- **MySQL** (Banco de dados relacional)  
- **Scene Builder** (Construção de interfaces gráficas)  
