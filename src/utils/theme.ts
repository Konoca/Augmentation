import { flavors } from '@catppuccin/palette';
import { colors, _updateColors } from '../constants';

//export const colors = flavors.mocha.colors;

const flavs = Object.keys(flavors);
let currentFlavor = flavs.indexOf('mocha');
export const changeTheme = () => {
    currentFlavor++;
    if (currentFlavor >= flavs.length) currentFlavor = 1;
    if (currentFlavor === 0 ) currentFlavor = 1;
    const f = flavs[currentFlavor] as keyof typeof flavors;
    _updateColors(flavors[f].colors);
}
