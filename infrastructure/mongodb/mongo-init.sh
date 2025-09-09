set -e
mongosh <<EOF
use admin;
db = db.getSiblingDB('blacklist');
db.createCollection('ibans');
db.createUser({
  user: '$MONGO_DB_USER_LOGIN',
  pwd:  '$MONGO_DB_USER_PASSWORD',
  roles: [{
    role: 'readWrite',
    db: '$MONGO_DB'
  }]
});
db.ibans.insertMany(
  [
    "BE68539007547034",
    "CH9300762011623852957",
    "DE15300606010505780780",
    "DE89370400440532013000",
    "FR1420041010050500013M02606",
    "NL91ABNA0417164300"
  ].map(givenIban => ({ iban: givenIban }))
);
EOF
