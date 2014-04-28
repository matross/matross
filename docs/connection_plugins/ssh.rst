SSH
============

The SSH connection lets you connect to remote systems over the secure
shell protocol.

Example
~~~~~~~

.. code-block:: clojure

  {:type :ssh
   :hostname "example.com"
   :port 2222}

Configuration
~~~~~~~~~~~~~

``:hostname``

  The hostname of the remote machine to connect to.

``:port``

  Defaults to ``22``.

``:username``

  Defaults to current username.

``:private-key-path``

  By default will use ``~/.ssh/id_rsa``

``:public-key-path``

  By default will use ``~/.ssh/id_rsa.pub``

In theory this also supports all options that can be passed to ``ssh
-o``, but this has not been tested.

Resources
~~~~~~~~~

- `Connection Source`_

.. _Connection Source: https://github.com/matross/matross/blob/master/plugins/matross/connections/ssh.clj
