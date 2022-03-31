# FindStuffer
FindStuffer, a Burp extension to Find Stuff.
## Why FindStuffer
When using Brup's built-in search modals in the Proxy or the Logger tabs, there are some limitations. This extension aims to overcome these limitations, with no intention to replace the original Burp search functionalities (because why would I work to do that ?)
## Demo & How to use
TODO
## Installation
At this moment, you need to compile this code to create a JAR and import manually from Extender → Extensions → Add.

A JAR will be added soon to this project's releases page. I still haven't looked into adding this to the BApp store.
## Use cases
Some of the situations that led me to coding this. These happened in projects containing 10k-20k+ requests.
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
I am working on a project including many domains, and I want to apply a text query on the body of requests targeting only a specific domain.

## Contributing
If you like the extension and the potential it has to be practical tool you use often, you are very welcome to contribute to this repo by submitting a PR. You can also open issues if you encounter bugs or have interesting feature ideas.

But before doing that, please read this section carefully to get an idea of what's planned / what's already being currently worked on.
### Known issues
The search modal always pops up on the top left corner of the screen. I don't know how to center it properly. Halp plis.
### Implemented Features
- Choosing the scope of a text query: request, response, both or any.
- Chaining text queries (aggregation using boolean AND logic), similar to piping grep commands together on UNIX.
### Current issues
This section is about features that are currently being developed / officially on the roadmap:
- [ ]  Implement "negative queries" per text field
- [ ]  Allow the aggregation of queries using boolean OR logic instead of a boolean AND
- [x]  Implement the ability to delete a text field. The only way of deleting unused text fields is by unloading and reloading the extension.
### Dev Ideas
This section is about features that I recognize as potentially useful and intend to work on some time, without them being a priority. Some of these features can be replaced by celver use of the basic features already implemented. Some do add a lot of value but will need a serious amount of work.
#### Listeners
It would be nice if the extension registers a proxy or HTTP listener to refresh the tables entires automatically as new HTTP traffic is generated. This however must:
- be thread-safe
- take into account existing sorting and filtering
- be memory-efficient
A clever implementation of this could talke some serious amount of work.
#### Global negative search
Besides the negative query option available for each individual text field, implement the possibility of inverting the whole search.
#### Columns list
Choose which columns can be displayed / hidden. Also add more types of columns (e.g. mime types).
#### Non-textual queries
This is not the aim of this extension, but might as well make it more practical.
#### Improve UI and UX
This is really not my area and I hate it. But my extension is indeed ugly.
### Dev & debug env setup
TODO