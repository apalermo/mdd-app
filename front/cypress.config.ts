import { defineConfig } from 'cypress';
import * as mysql from 'mysql2/promise';
import * as fs from 'fs';
import * as path from 'path';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    setupNodeEvents(on, config) {
      on('task', {
        async resetDb() {
          const connection = await mysql.createConnection({
            host: 'localhost',
            user: config.env.DB_USER,
            password: config.env.DB_PASSWORD,
            database: config.env.DB_NAME,
            multipleStatements: true,
          });

          try {
            const sqlPath = path.join(__dirname, 'cypress/data/reset-db.sql');
            const sql = fs.readFileSync(sqlPath, 'utf8');
            await connection.query(sql);
            return null;
          } catch (err) {
            console.error('Erreur Task resetDb:', err);
            throw err;
          } finally {
            await connection.end();
          }
        },
      });
    },
  },
});
