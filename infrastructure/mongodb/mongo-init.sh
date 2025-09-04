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
    "FR14 2004 1010 0505 0001 3M02 606",
    "DE89 3704 0044 0532 0130 00",
    "NL91 ABNA 0417 1643 00",
    "BE68 5390 0754 7034",
    "CH93 0076 2011 6238 5295 7",
    "DE15 3006 0601 0505 7807 80"
  ].map(givenIban => ({ iban: givenIban }))
);
EOF
