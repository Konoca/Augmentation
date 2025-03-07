import { Typography } from '@mui/material';
import { styled } from '@mui/material/styles';
import { colors } from '../constants';

export const CustomTitle = styled(Typography)({
    color: colors.text.hex,
});

export const CustomSubTitle = styled(Typography)({
    color: colors.subtext1.hex,
});
