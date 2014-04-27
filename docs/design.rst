Design
======

Quick and dirty high level architecture & design goals, *please patch*
this!!

Make something that is actually useful. Don't compromise simplicity
for convenience. Make automation natural and complex things possible.
Simple means single responsiblity, without interleaving or overlapping
parts.

- simple automation

  - reference based template system
  - immutable, always available data
  - scalable configuration (1 machine or 1k machines)

- simple to extend

  - plugin system
  - designed around protocols

- reusable and composable

  - plugins
  - configurations
  - operations

- flexible

  - with or without sudo
  - able to work on any type of connection
  - agented or agentless

Keep in mind this is not a feature list but instead a general
direction. Change as you see fit.
