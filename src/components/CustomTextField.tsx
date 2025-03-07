import { TextField } from '@mui/material';
import { styled } from '@mui/material/styles';
import { colors } from '../constants';

const CustomTextField = styled(TextField)({
    '& label': {
        color: colors.text.hex,
    },
    '& label.Mui-focused': {
        color: colors.text.hex,
    },
    '& .MuiOutlinedInput-root': {
        color: colors.text.hex,
        '& fieldset': {
            borderColor: colors.overlay0.hex,
        },
        '&:hover fieldset': {
            borderColor: colors.overlay1.hex,
        },
        '&.Mui-focused fieldset': {
            borderColor: colors.lavender.hex,
        },
    },
});

export default CustomTextField;
