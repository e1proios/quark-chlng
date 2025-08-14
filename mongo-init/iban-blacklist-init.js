db = db.getSiblingDB('blacklist');
db.createCollection('ibans');
db.createUser({
  user: "user",
  pwd: "password",
  roles: [{ role: "read", db: "blacklist" }]
});

// Create collections and insert data
db.ibans.insertMany(
  [
    "FR14 2004 1010 0505 0001 3M02 606",
    "DE89 3704 0044 0532 0130 00",
    "NL91 ABNA 0417 1643 00",
    "BE68 5390 0754 7034",
    "CH93 0076 2011 6238 5295 7"
  ].map(givenIban => ({ iban: givenIban }))
);
