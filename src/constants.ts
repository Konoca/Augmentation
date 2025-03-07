import { flavors } from '@catppuccin/palette'

export let colors = flavors.mocha.colors;

export const _updateColors = (newColors: typeof colors) => {
    colors = newColors;
}
