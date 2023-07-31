import { Component, Show } from "solid-js";
import styles from "./Card.module.scss";
import leftArrow from "../assets/left-arrow.svg";
import rightArrow from "../assets/right-arrow.svg";
import saveList from "../assets/save-list.svg";
import saveListBg from "../assets/save-list-bg.svg";

export type CardType = Word & {
  showDefi: boolean;
  isForgot: boolean;
};
type CardProps = {
  handleFlip: (id: number, e: Event) => void;
  toggleForgotBtn: (id: number, e: Event) => void;
  goNextCard: () => void;
  goPrevCard: () => void;
  title: string;
  word: CardType;
  id: number;
};
const Card: Component<CardProps> = ({
  handleFlip,
  toggleForgotBtn,
  title,
  word,
  goNextCard,
  goPrevCard,
  id,
}) => {
  return (
    <div class={styles.flipCard}>
      <div
        classList={{
          [styles.card]: true,
          [styles.rotateCard]: word.showDefi,
        }}
        onClick={[handleFlip, id]}
      >
        <div class={styles.cardInnerFront}>
          {/* card head */}
          <div class={styles.cardHeader}>
            <small>{title}</small>
          </div>
          {/* card body */}
          <div class={styles.cardBody}>
            <p>{word.text}</p>
          </div>
          {/* card food */}
          <div class={styles.cardFooder}>
            <button onClick={goPrevCard} tabIndex={-1}>
              <img src={leftArrow} alt="Go back" />
            </button>
            <button
              classList={{ [styles.forgotActive]: word.isForgot }}
              onClick={[toggleForgotBtn, id]}
              title="Didn't remember this word? Save it to review later"
              tabIndex={-1}
            >
              Again
            </button>
            <button onClick={goNextCard} tabIndex={-1}>
              <img src={rightArrow} alt="Go next" />
            </button>
          </div>
        </div>

        <div class={styles.cardInnerBack}>
          {/* card head */}
          <div class={styles.cardHeader}>
            <small>{title}</small>
          </div>
          {/* card body */}
          <div class={styles.cardBody}>
            <p>{word.definition}</p>
          </div>
          {/* card food */}
          <div class={styles.cardFooder}>
            <button onClick={goPrevCard} tabIndex={-1}>
              <img src={leftArrow} alt="Go back" />
            </button>
            <button
              classList={{ [styles.forgotActive]: word.isForgot }}
              onClick={[toggleForgotBtn, id]}
              title="Didn't remember this word? Save it to review later"
              tabIndex={-1}
            >
              Again
            </button>
            <button onClick={goNextCard} tabIndex={-1}>
              <img src={rightArrow} alt="Go next" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Card;
