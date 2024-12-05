// Importações específicas do Firebase Modular SDK
const { initializeApp } = require('firebase/app');
const { getFirestore } = require('firebase/firestore');

// Configuração do Firebase
const firebaseConfig = {

  apiKey: "AIzaSyDhZ0jNSrvphkJlCwOQXorBcvK-ZzPVkrY",

  authDomain: "finalproject-c4d5d.firebaseapp.com",

  projectId: "finalproject-c4d5d",

  storageBucket: "finalproject-c4d5d.firebasestorage.app",

  messagingSenderId: "961810668212",

  appId: "1:961810668212:web:a54447751dd86c721795e1",

  measurementId: "G-4P6YCDZNCS"

};


// Inicializa o app Firebase e o Firestore
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

module.exports = db;
