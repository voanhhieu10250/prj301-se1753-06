import HashTable from "./HashTable";

class DataStore {
  private _data: HashTable<string, string>;
  private _keys: string[] = [];

  constructor() {
    this._data = new HashTable<string, string>();
  }

  setData(data: DataItem[]) {
    data.forEach((word) => {
      this._data.add(word.front, word.back);
      this._keys.push(word.front);
    });
  }

  getDataSize(): number {
    return this._data.size;
  }

  getData(start?: number, perPage?: number, addOnProps?: object): Word[] {
    const result: Word[] = [];
    const props = addOnProps || {};
    const startIdx = start || 0;
    const perPageIdx =
      perPage && perPage <= this._data.size ? perPage : this._data.size;

    for (let i = startIdx; i < perPageIdx + startIdx; i++) {
      if (!this._keys[i]) break;
      result.push({
        text: this._keys[i],
        definition: this._data.get(this._keys[i]) || "",
        ...props,
      });
    }
    return result;
  }
}

export default DataStore;
