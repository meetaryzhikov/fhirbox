#!/bin/bash

# Start tmux session for efficient Clojure development
tmux new-session -d -s dev

# Window 1: Editor
tmux rename-window -t dev:1 'editor'
tmux send-keys -t dev:1 'vim .' C-m

# Window 2: REPL
tmux new-window -t dev:2 -n 'repl'
tmux send-keys -t dev:2 'clj' C-m

# Window 3: Tests
tmux new-window -t dev:3 -n 'tests'
tmux send-keys -t dev:3 'clojure -M:test/watch' C-m

# Window 4: Server
tmux new-window -t dev:4 -n 'server'
tmux send-keys -t dev:4 'docker-compose up -d' C-m
tmux send-keys -t dev:4 'sleep 10' C-m
tmux send-keys -t dev:4 './server.sh start' C-m

# Window 5: Git and monitoring
tmux new-window -t dev:5 -n 'git'
tmux split-window -h -t dev:5
tmux send-keys -t dev:5.0 'htop' C-m
tmux send-keys -t dev:5.1 'lazygit' C-m

# Window 6: Docker monitoring
tmux new-window -t dev:6 -n 'docker'
tmux send-keys -t dev:6 'lazydocker' C-m

# Attach to session
tmux attach-session -t dev