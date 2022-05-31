export const getRandomColor = () =>
  `#${(0x1000000 + Math.random() * 0xffffff).toString(16).substring(1, 7)}`;
