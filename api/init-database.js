use('users');
db.createCollection('usuarios');
db.createUser({
    user: "admin",
    pwd: "admin123",
    roles: [{ role: "readWrite", db: "users" }]
});