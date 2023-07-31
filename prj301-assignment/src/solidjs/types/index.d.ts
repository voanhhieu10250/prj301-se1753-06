export {};

declare global {
  type Word = {
    text: string;
    definition: string;
  };
  type DataInfo = {
    size: number;
  };
  type DataItem = {
    cardId: number;
    front: string;
    back: string;
    tags: string;
    deckId: number;
  };
}
