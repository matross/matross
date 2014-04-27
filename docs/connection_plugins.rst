Connection Plugins
==================

Connections are core to the design and operation of matross. Using a
simple process interface matross can connect to a remote system and
execute commands. Built in connection types include ``local`` and
``ssh`` connections, but the system is designed to be extended.

====================
Connection Interface
====================

Matross connections must implement two lifecycle functions:

.. code-block:: clojure

   ; IConnect
   (connect    [self] "connect to a machine")
   (disconnect [self] "disconnect from a machine")

And implement a single entry point:

.. code-block:: clojure

   ; IRun
   (run [self process] "execute a command on a machine")

=============
Process Model
=============

The contract of implementing ``run`` for a connection is that given a
process specification as input, a representation of a running process
will be returned. Above ``process`` is a map containing the keys
``:cmd`` and optionally ``:in``. The command is a sequence of a
program to run and any number of positional arguments.

.. code-block:: clojure

   ;; list representing a command to be run
   ["/bin/sh" "-c" "seq 10 | tail -n 50"]

A processes ``:in`` should be either a ``String`` or an ``InputStream``.

.. code-block:: clojure

   {:in "hello world" :cmd ["cat"]}

The result of executing ``run`` should be a representation of a
process: specifically two streams, stdout & stderr, and a future of
the process exit status.

.. code-block:: clojure

   { :out  InputStream
     :err  InputStream
     :exit FutureExitCode }

=======================
Registering Connections
=======================

In order to register with the matross runner a connection must
implement the ``get-connection`` multimethod.

.. code-block:: clojure

   ; matross.connections.core/get-connection
   (defmethod get-connection :my-type [connection-spec]
     (build-my-connection connection-spec))

The registered function is expected to return something that satisfies
both the `IConnect` and `IRun` protocols. The input is a user-provided
map representing the connection, the only hard requirement being that
the ``:type`` of the map matches your dispatch key (``:my-type`` in
the example above).

.. code-block:: clojure

   ;; example connection spec
   { :type :docker :image "ubuntu" }

The docker connection type doesn't exist, you should implement it!

==================
External Resources
==================

- `Connection Interface`_
- `Local Connection`__

.. _Connection Interface: https://github.com/matross/matross/blob/master/src/matross/connections/core.clj

__ https://github.com/matross/matross/blob/master/plugins/matross/connections/local.clj
