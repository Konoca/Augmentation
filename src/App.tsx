import React from 'react';
import './App.css';
import { Box, TextField, Typography } from '@mui/material';

function App() {
  const [modsPath, setModsPath] = React.useState<string>('');

  const handleChange = (path: string) => {
    setModsPath(path);
  };

  return (
    <Box sx={{ width: '100%', height: '100%' }} display={'flex'} flexDirection={'column'}>
      <Typography variant='h1' align='center' gutterBottom className='title'>
        Augmentation
      </Typography>
      <Typography variant='h4' align='center' gutterBottom className='subtitle'>
        A Tool Made By Me :)
      </Typography>

      <TextField
        required
        id="textfield"
        label="Path/To/.minecraft/mods"
        sx={{ my: 2 }}
        fullWidth
        value={modsPath}
        onChange={(event) => handleChange(event.target.value)}
      />
    </Box>
  );
}

export default App;
