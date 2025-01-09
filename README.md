[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/ori1I0wD)

## Como executar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/imetropoledigital/trabalho-ii-unidade-thithu
   cd trabalho-ii-unidade-thithu
   ```
2. Execute o projeto com Docker Compose

```bash
docker-compose up --build
```

3. Acesse a aplicação
- A API estará disponível em: <a href="http://localhost:8080/">locahost:8080</a>

## Rotas disponíveis
1. POST <strong>/{collectionName}</strong> </br>
Exemplo de requisição: </br>
POST /users
Content-Type: application/json
```javascript
{
	name: "João",
	"age": 22
}
```
2. GET <strong>/{collectionName}?query={}&fields=campo1&page=0&size=5</strong> </br>
Os parâmetros de rota são opcionais. Caso não sejam passados, é devolvida todas as entidades.
3. GET <strong>/{collectionName}/id</strong> </br>
4. PUT <strong>/{collectionName}</strong> </br>
Exemplo de requisição: </br>
PUT /users/10
Content-Type: application/json
```javascript
{
	name: "Maria"
	age: 34
}
```