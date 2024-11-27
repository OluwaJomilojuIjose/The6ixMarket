const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
app.use(bodyParser.json());
app.use(cors());

// Database connection
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
});

// Connect to MySQL
db.connect((err) => {
    if (err) throw err;
    console.log('Connected to MySQL.');

    // Ensure the database exists
    db.query('CREATE DATABASE IF NOT EXISTS thesixmarket', (err, result) => {
        if (err) throw err;
        console.log('Database checked/created.');

        // Switch to the thesixmarket database
        db.query('USE thesixmarket', (err) => {
            if (err) throw err;
            console.log('Using thesixmarket database.');

            // Ensure the table exists
            const createTableQuery = `
            CREATE TABLE IF NOT EXISTS messages (
                id INT AUTO_INCREMENT PRIMARY KEY,
                chatRoomId VARCHAR(255),
                sender VARCHAR(255),
                message TEXT,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            `;
            db.query(createTableQuery, (err, result) => {
                if (err) throw err;
                console.log('Messages table checked/created.');
            });
        });
    });
});

// POST route to send a message
app.post('/messages', (req, res) => {
    const { chatRoomId, sender, message } = req.body;
    const query = 'INSERT INTO messages (chatRoomId, sender, message) VALUES (?, ?, ?)';
    db.query(query, [chatRoomId, sender, message], (err, result) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ success: false, message: 'Failed to send message.' });
        }
        res.json({ success: true, message: 'Message sent successfully!' });
    });
});

// GET route to retrieve messages
app.get('/messages/:chatRoomId', (req, res) => {
    const chatRoomId = req.params.chatRoomId;
    const query = 'SELECT * FROM messages WHERE chatRoomId = ? ORDER BY timestamp DESC';
    db.query(query, [chatRoomId], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ success: false, message: 'Failed to load messages.' });
        }
        res.json(results);
    });
});

// Start server
app.listen(3000, () => {
    console.log('Server running on port 3000');
});
