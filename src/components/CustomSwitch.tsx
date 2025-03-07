import { Switch, SwitchProps, FormControlLabel, styled } from '@mui/material';
import { colors } from '../constants';

const StyledSwitch = styled(Switch)({
    '& .MuiSwitch-colorPrimary': {
        color: colors.blue.hex,
        '&.Mui-checked + .MuiSwitch-track': {
            backgroundColor: colors.blue.hex,
        },
    },
    '& .MuiSwitch-track': {
        backgroundColor: colors.surface2.hex,
    },
});

function CustomSwitch(props: SwitchProps) {
    return (
        <FormControlLabel
            control={
                <StyledSwitch
                    {...props}
                />
            }
            label={'Delete .index?'}
            sx={{color: colors.text.hex}}
        />
    )
}

export default CustomSwitch;
