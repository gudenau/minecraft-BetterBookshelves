[![Demo Video](http://img.youtube.com/vi/kbiEJ1e54-k/0.jpg)](http://www.youtube.com/watch?v=kbiEJ1e54-k "Demo Video")

Adds some basic filtering capabilities to Chiseled Bookshelves:
- Can filter by enchantment level and book generation based on a Roman numeral suffix
- Can filter by enchantment ID or book author with an @ prefix
- Can do a fuzzy filter by prefixing or suffixing the name with an *

Some examples:
- "*Protection I", filters all "protection" enchanted books that are level 1. I.E. Protection I, Fire Protection I.
- "@gudenau I", filters all first generation books by author "gudenau"
- "@minecraft:feather_falling" filters all Feather Falling enchanted books

Note that the filtering is always a little fuzzy when it isn't for an author/enchant ID so you will have to be mindful of people throwing in a written book called "Protection" into your "Protection" filters.
