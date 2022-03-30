# FindStuffer
FindStuffer, a Burp extension that finds stuff. 
## Why FindStuffer
When using Brup's built-in search modals in the Proxy or the Logger tabs, there are some limitations. This extension aims to overcome these limitations, without aiming to replace the original Burp search functionalities (because why work too much ?)
## Demo
TODO
## Lists
### ToDo list
Here are the things that are meant to be implemented in FindStuffer :
- [x]  Choosing the scope of a text query: request, response, both or any (the latter being Burp's only built-in option AFAIK)
- [ ]  Chaining text queries, the same way one can pipe `grep` commands together on UNIX (`cat | grep aaaa | grep bbb | grep -v cccc`). This is basically combining the queries with a "boolean and" logic.

### Bonus list
Bonus objectives are objectives that I recognize as "nice", but am not motivated enough to code : 
- [ ] BONUS: Make the extension less ugly. I hate coding UI.
- [ ] BONUS: Filtering on hosts. Can be done with a normal text query, but would be faster with a dedicated field.
- [ ] BONUS: Combining text queries with a "boolean or" logic. I think it's more stylish than useful.
- [ ] BONUS: Re-implement some basic non-textual filtering capabilities like filtering over response codes. Burp's built-in search modal does that fine.
- [ ] BONUS: Implementing regex-based searches. It might be a pain to code.

### ToNotDo list
This is the list of things I have **really** no intention of doing
- Coding **_all_** of Burp's existing search options, because that's a waste of time
- Adding a proxy or HTTP listener, because it adds work without any real value to the use cases

If you want something added to FindStuffer that you think would be useful, feel free to suggest a PR or open an issue.
## Installation
TODO
## Contribution
TODO
## Known issues
TODO
## Use cases
Some of the situations that led me to coding this, in projects containing 10k-20k+ requests.
### Use case 1
I wanted to look for HTTP responses that contained the `Strict-Transport-Security` header, but did **not** have 31536000 configured for `max-age`. I solved it by doing a regex search with negative lookahead (`^Strict-Transport-Security(?!.*31536000.*)`), which was annoying because:
- I do not use negative lookaheads a lot, so I have to research them every time I need them.
- Regex-based searches are slow, and force Burp to convert data from bytes format to String, which also costs a bit of memory.

With FindStuffer, the idea would be to do two text queries in one search:
1. One query over responses asking for `Strict-Transport-Security`
2. A second inverted query over the responses for 31536000

I find this to be more intuitive and it is probably less costly than the regex solution.
### Use case 2
I wanted to find an HTTP response containing some word (let's say, baguette), but only during a certain session, identified by a specific value for PHPSESSID. I didn't find a practical way to do this in Burp. I did a first search on the PHPSESSID value I wanted, highlighted all the results, did another query on baguette, and scrolled away looking for highlighted entries.

Hopefully, with FindStuffer, this kind of thing will be pretty easy to handle, since both queries can be combined in one search.
### Use case 3
I am working on a project including many domains, and I won't to do a search query only on requests targeting a specific domain.

