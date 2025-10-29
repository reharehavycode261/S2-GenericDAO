const express = require('express');
const router = express.Router();
const Database = require('../database/core/Database');

router.get('/search', async (req, res) => {
    const criteria = req.query.criteria;
    try {
        const results = await performDynamicSearch(criteria);
        res.json(results);
    } catch (error) {
        console.error('Erreur lors de la recherche:', error);
        res.status(500).json({ error: 'Erreur lors de la recherche.' });
    }
});

async function performDynamicSearch(criteria) {
    // Exemple simplifié: Construire une requête SQL dynamique à partir des critères
    let query = 'SELECT * FROM items WHERE ' + criteria; // ATTENTION: Dans la vraie vie, utilisez des instructions préparées pour éviter les injections SQL
    const db = new Database();
    return await db.executeQuery(query);
}

module.exports = router;