# Time Travel Clj

Time travel Clj is a toy shell with an immutable filesystem.  You can create and delete files and directories and "undo" or "redo" your changes.  It works by taking advantage of Clojure's immutable data structures, and building a filesystem on top of them.

Use for educational purposes.

## Demo

```
lein run
...
time-travel-clj $ ls
=> <nothing>
time-travel-clj $ mkdir home
time-travel-clj $ ls
home/
time-travel-clj $ rewind-by 1
time-travel-clj $ ls
=> <nothing>
time-travel-clj $ fast-forward-by 1
time-travel-clj $ ls
home/
```

## Usage

```
lein run
```

Will bring up a prompt that looks like:

```
time-travel-clj $
```

## Testing

```
lein test
```

## Repl Development
To speed up development, you can use Clojure's repl:

```
lein repl
```

which starts the REPL: 

```
nREPL server started on port 58024 on host 127.0.0.1 - nrepl://127.0.0.1:58024
REPL-y 0.3.5, nREPL 0.2.6
Clojure 1.6.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_25-b17
Docs: (doc function-name-here)
      (find-doc "part-of-name-here")
      Source: (source function-name-here)
      Javadoc: (javadoc java-object-or-class-here)
      Exit: Control+D or (exit) or (quit)
      Results: Stored in vars *1, *2, *3, an exception in *e
time-travel-clj.core=>
```

You can run tests in the REPL with: 

```
time-travel-clj.core=> (require 'time-travel-clj.core :reload-all)
nil
time-travel-clj.core=> (clojure.test/run-tests)
```

If you make changes to `src/`, you can just do the first command again to reload all files, instead of incurring the cost of `lein test` or restarting the repl.

