import styles from "./App.module.scss";
import {
  Component,
  createSignal,
  onMount,
  batch,
  createEffect,
  on,
  Show,
  For,
} from "solid-js";
import { createStore } from "solid-js/store";
import { Swiper, SwiperSlide } from "swiper/solid";
import type { Swiper as SwiperRef } from "swiper";
import { Navigation, EffectCreative, Keyboard } from "swiper";
import Card, { CardType } from "./components/Card";
import DataStore from "../utils/DataStore";

const apiPath = document
  .getElementById("apiPath")
  ?.getAttribute("data-path") as string;

const homePath = document
  .getElementById("homePath")
  ?.getAttribute("data-path") as string;

const deckName = document
  .getElementById("deckName")
  ?.getAttribute("data-value") as string;

async function fetchData(dataStore: DataStore) {
  const res = await fetch(
    process.env.NODE_ENV === "development"
      ? "http://localhost:8080/prj301-assignment/decks/play-deck?id=11&user_id=1"
      : apiPath,
    {
      method: "POST",
    }
  );
  if (!res.ok) {
    throw new Error("Error: " + res.status);
  }
  const data = await res.json();
  dataStore.setData(data);
}

const isButton = (element: HTMLElement) => {
  let el: HTMLElement | null = element;
  if (el.hasChildNodes()) {
    for (let i = 0; i < el.childNodes.length; i++) {
      const child = el.childNodes[i];
      if (child.nodeName === "BUTTON") {
        return false;
      }
    }
  }
  for (; el !== null && el.nodeName && !(el.nodeName === "BUTTON"); )
    el = el.parentNode as HTMLElement | null;
  return !!el;
};
function shuffleArray(array: unknown[]) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
}
const App: Component = () => {
  const dataStore = new DataStore();
  const [dbSize, setDbSize] = createSignal(0);
  const [cardsPerRound, setCardsPerRound] = createSignal<number>(20);
  const [swiper, setSwiper] = createSignal<SwiperRef | null>(null);

  const [words, setWords] = createStore<CardType[]>([]);
  const [forgotIndexes, setForgotIndexes] = createSignal<number[]>([]);

  const [isShowResult, setIsShowResult] = createSignal<boolean>(false);
  const [isShowForgot, setIsShowForgot] = createSignal<boolean>(false);

  const [isShuffled, setIsShuffled] = createSignal<boolean>(true);
  const [showDefiFirst, setShowDefiFirst] = createSignal<boolean>(false);

  onMount(async () => {
    await fetchData(dataStore);
    const dataSize = dataStore.getDataSize();
    const result = dataStore.getData(undefined, cardsPerRound(), {
      showDefi: showDefiFirst(),
      isForgot: false,
    });
    if (isShuffled()) shuffleArray(result);

    batch(() => {
      setDbSize(dataSize);
      setWords(result as CardType[]);
    });
  });

  createEffect(
    on(swiper, (swiper) => {
      // Swiper events only work after swiper is initialized
      // If you want to use swiper events, you need to use createEffect
      if (!swiper) return;
      swiper.on("keyPress", (s, keyCode) => {
        // right arrow
        if (s.isEnd && parseInt(keyCode) === 39) {
          batch(() => {
            setIsShowForgot(false);
            setIsShowResult(true);
          });
        }
        // enter, up arrow
        if (parseInt(keyCode) === 13 || parseInt(keyCode) === 38) {
          if (isShowForgot())
            setWords(
              forgotIndexes()[swiper.activeIndex],
              "showDefi",
              (showDefi) => !showDefi
            );
          else {
            if (isShowResult()) return;
            setWords([swiper.activeIndex], "showDefi", (showDefi) => !showDefi);
          }
        }
        // down arrow, space
        if (parseInt(keyCode) === 40 || parseInt(keyCode) === 32) {
          if (isShowForgot())
            toggleForgotBtn(forgotIndexes()[swiper.activeIndex]);
          else {
            if (isShowResult()) return;
            toggleForgotBtn(swiper.activeIndex);
          }
        }
      });
    })
  );

  // Update forgotWords right after the review round ends
  createEffect(
    on(
      isShowForgot,
      (isShowForgot) => {
        if (!isShowForgot) {
          setForgotIndexes(
            forgotIndexes().filter((idx) => words[idx].isForgot)
          );
        }
      },
      { defer: true }
    )
  );

  // Update words when shuffle changes.
  // createEffect(
  //   on(
  //     isShuffled,
  //     (isShuffle) => {
  //       if (isShowForgot()) return;
  //       const result = dataStore.getData(undefined, words.length, {
  //         showDefi: showDefiFirst(),
  //         isForgot: false,
  //       });

  //       if (isShuffle) shuffleArray(result);

  //       batch(() => {
  //         setWords(result as CardType[]);
  //         setForgotIndexes([]);
  //       });
  //       swiper()?.update();
  //       // swiper()?.slideTo(0, 0);
  //     },
  //     { defer: true }
  //   )
  // );

  const handleShuffleChange = () => {
    if (isShowForgot()) return;
    const result = dataStore.getData(undefined, words.length, {
      showDefi: showDefiFirst(),
      isForgot: false,
    });

    if (isShuffled()) shuffleArray(result);

    batch(() => {
      setWords(result as CardType[]);
      setForgotIndexes([]);
    });
    swiper()?.update();
    // swiper()?.slideTo(0, 0);
  };

  const handleCardPerRoundChange = (e: Event) => {
    const target = e.target as HTMLSelectElement;
    const value = target.value === "full" ? undefined : parseInt(target.value);
    const result = dataStore.getData(undefined, value, {
      showDefi: showDefiFirst(),
      isForgot: false,
    });
    if (isShuffled()) shuffleArray(result);

    batch(() => {
      setCardsPerRound(value ?? dbSize());
      setWords(result as CardType[]);
    });
    swiper()?.update();
  };

  const goNextCard = () => {
    swiper()?.slideNext();
    if (swiper()?.isEnd) {
      batch(() => {
        setIsShowForgot(false);
        setIsShowResult(true);
      });
    }
  };
  const goPrevCard = () => {
    swiper()?.slidePrev();
  };
  const handleReviewForgot = () => {
    batch(() => {
      setIsShowResult(false);
      setIsShowForgot(true);
      setWords(forgotIndexes(), "showDefi", showDefiFirst());
    });
    swiper()?.update();
    swiper()?.slideTo(0, 0);
  };
  const handleNextRound = () => {
    const oldLength = words.length;
    const result = dataStore.getData(words.length, cardsPerRound(), {
      showDefi: showDefiFirst(),
      isForgot: false,
    });
    if (isShuffled()) shuffleArray(result);

    batch(() => {
      setIsShowResult(false);
      setIsShowForgot(false);
      setWords([...words, ...(result as CardType[])]);
    });
    swiper()?.update();
    swiper()?.slideTo(oldLength, 0);
  };
  const handleRemoveForgotCards = () => {
    batch(() => {
      setWords(forgotIndexes(), {
        isForgot: false,
        showDefi: showDefiFirst(),
      });
      setForgotIndexes([]);
    });
  };
  const handleFlip = (id: number, e: Event) => {
    if (!isButton(e.target as HTMLElement)) {
      setWords([id], "showDefi", (showDefi) => !showDefi);
    }
  };
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const toggleForgotBtn = (id: number, _?: Event) => {
    batch(() => {
      if (!isShowForgot()) {
        if (!words[id].isForgot && !forgotIndexes().includes(id))
          setForgotIndexes([...forgotIndexes(), id]);
        if (words[id].isForgot && forgotIndexes().includes(id))
          setForgotIndexes(forgotIndexes().filter((idx) => idx !== id));
      }
      setWords([id], "isForgot", (isForgot) => !isForgot);
    });
  };
  const toggleShuffle = (t: Event) => {
    if (words.length >= 400) {
      if (
        confirm(
          "This function is expensive on this amount of cards. All forgotten cards will be reset. Are you sure?"
        )
      ) {
        (t.target as HTMLInputElement).checked = !isShuffled();
        setIsShuffled((val) => !val);
        handleShuffleChange();
      } else {
        (t.target as HTMLInputElement).checked = isShuffled();
      }
    } else {
      if (forgotIndexes().length > 0) {
        if (confirm("All forgotten cards will be reset. Are you sure?")) {
          (t.target as HTMLInputElement).checked = !isShuffled();
          setIsShuffled((val) => !val);
          handleShuffleChange();
        } else {
          (t.target as HTMLInputElement).checked = isShuffled();
        }
      } else {
        (t.target as HTMLInputElement).checked = !isShuffled();
        setIsShuffled((val) => !val);
        handleShuffleChange();
      }
    }
  };
  const handleShuffleForgottenCards = () => {
    const newList = forgotIndexes().splice(0);
    shuffleArray(newList);
    setForgotIndexes(newList);
    swiper()?.update();
  };
  const toggleShowDefiFirst = () => {
    batch(() => {
      setShowDefiFirst((val) => !val);
      setWords({}, "showDefi", showDefiFirst());
    });
  };
  const list = () =>
    !isShowForgot()
      ? words.splice(0)
      : forgotIndexes().map((idx) => words[idx]);

  return (
    <div class={styles.container}>
      <div class="flex gap-5 justify-center items-center">
        <Show
          when={!isShowForgot()}
          fallback={
            <div class="flex justify-center">
              <button
                type="button"
                class={styles.shuffleForgotten}
                onClick={handleShuffleForgottenCards}
              >
                Shuffle cards
              </button>
            </div>
          }
        >
          <div class={styles.shuffle}>
            <input
              type="checkbox"
              id="shuffle"
              checked={isShuffled()}
              onChange={toggleShuffle}
            />
            <label
              for="shuffle"
              style={{
                "text-decoration": isShuffled() ? "none" : "line-through",
              }}
              title="Shuffle cards based on rounds"
            >
              Shuffle is on
            </label>
          </div>
        </Show>
        <div class={styles.defiFirst}>
          <input
            type="checkbox"
            id="showDefiFirst"
            checked={showDefiFirst()}
            onChange={toggleShowDefiFirst}
          />
          <label
            for="showDefiFirst"
            style={{
              "text-decoration": showDefiFirst() ? "none" : "line-through",
            }}
            title="Show definition first"
          >
            Show definition first
          </label>
        </div>
      </div>
      <h1>
        <span class="max-w-xs overflow-hidden truncate">{deckName}</span> (round{" "}
        {Math.round(words.length / cardsPerRound())}/
        {Math.round(dbSize() / cardsPerRound())})
      </h1>
      <div class={styles.roundOption}>
        <label for="cardsPerRound">Cards per round:&nbsp;</label>
        <select
          name="cardPerRound"
          title="Card per round"
          onChange={handleCardPerRoundChange}
          tabIndex={-1}
        >
          <option value="20">20</option>
          <option value="30">30</option>
          <option value="50">50</option>
          <option value="full">Full</option>
        </select>
        <p>&nbsp;/ {dbSize()}</p>
      </div>
      <div class={styles.slider}>
        <Show when={!isShowResult()}>
          <Swiper
            tabIndex={-1}
            modules={[Navigation, EffectCreative, Keyboard]}
            initialSlide={0}
            keyboard={{
              enabled: true,
              pageUpDown: false,
            }}
            navigation={{
              prevEl: null,
              nextEl: null,
            }}
            effect="creative"
            creativeEffect={{
              prev: {
                translate: ["-120%", 0, -500],
              },
              next: {
                translate: ["120%", 0, -500],
              },
            }}
            speed={300}
            allowTouchMove={false}
            spaceBetween={30}
            slidesPerView={1}
            onSwiper={(swiper) => setSwiper(swiper)}
            onDestroy={() => setSwiper(null)}
          >
            <For each={list()}>
              {(word, idx) => {
                return (
                  <SwiperSlide>
                    <Card
                      word={word}
                      title={`${idx() + 1}/${list().length}`}
                      handleFlip={handleFlip}
                      toggleForgotBtn={toggleForgotBtn}
                      goNextCard={goNextCard}
                      goPrevCard={goPrevCard}
                      id={!isShowForgot() ? idx() : forgotIndexes()[idx()]}
                    />
                  </SwiperSlide>
                );
              }}
            </For>
            {/* we need this empty SwiperSlide to do some functions on last slide */}
            <SwiperSlide></SwiperSlide>
          </Swiper>
        </Show>
        <Show when={isShowResult()}>
          <div class={styles.endRound}>
            <div class={styles.modal}>
              <h3>End of round</h3>
              <div class="flex-1 flex justify-center items-center flex-col">
                <p>Words saved for review: {forgotIndexes().length} words</p>
                <Show when={forgotIndexes().length === 0}>
                  <p>Good Job!</p>
                </Show>
              </div>
              <div class={styles.buttons}>
                <Show when={forgotIndexes().length > 0}>
                  <button onClick={handleReviewForgot}>
                    Review forgotten cards
                  </button>
                  <button onClick={handleRemoveForgotCards}>
                    Clean forgotten cards
                  </button>
                </Show>
                <Show
                  when={words.length !== dbSize()}
                  fallback={
                    <div class="flex justify-center w-full">
                      <a href={homePath} class={styles.backToMenu}>
                        Back to home
                      </a>
                    </div>
                  }
                >
                  <button onClick={handleNextRound}>Next round</button>
                </Show>
              </div>
            </div>
          </div>
        </Show>
      </div>
    </div>
  );
};

export default App;
