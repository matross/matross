#!/bin/sh

env TEST_LOCAL=1 TEST_DEBUG=1 lein2 do test, test :integration
