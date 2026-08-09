export default function CategoryChips({ categories, selected, onSelect }) {
  return (
    <div className="category-chips">
      <button
        className={`chip-pill ${selected === '' ? 'chip-pill-active' : ''}`}
        onClick={() => onSelect('')}
      >
        All
      </button>
      {categories.map((c) => (
        <button
          key={c.categoryId}
          className={`chip-pill ${selected === String(c.categoryId) ? 'chip-pill-active' : ''}`}
          onClick={() => onSelect(String(c.categoryId))}
        >
          {c.name}
        </button>
      ))}
    </div>
  );
}
