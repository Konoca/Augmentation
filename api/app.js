const express = require('express');
const cors = require('cors');

const osRouter = require('./routes/os');

const PORT = 3001;

const app = express();
app.use(cors());
app.use(express.json());

app.use('/os', osRouter);

// health check
app.get('/', (req, res) => {
    res.send('hello world!');
});

app.listen(PORT, () => {
    console.log(`Server is listening at http://localhost:${PORT}`);
});
