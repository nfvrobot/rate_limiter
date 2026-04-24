# Rate Limiter
![Java](https://img.shields.io/badge/Java-25-orange)
![Status](https://img.shields.io/badge/status-experimental-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)
<p align="center">
  <img src="public/oh_hi_mark.gif" alt="Community vibe" />
</p>

## TL;DR

This project exists because I had some free time, a technical idea, and a complete lack of understanding what to do with spare time.
And not enough peace to simply relax.

So instead of touching grass, I started building a Java rate limiter.

If that sounds like your kind of open source energy — you are very welcome here.

## Welcome
Hi everyone, my name is Denis, I’m a Software Engineer.

This project started as a technical idea and time killer, but it eventually becomes something... important? A way to connect with people through open source.  
I want this repository to be a place where anyone interested in Java (yeap, especially me), software design, concurrency, and backend engineering can join, contribute, share ideas, and do whatever you want (only legal stuff).

If this topic is interesting to you, feel free to explore, open discussions, suggest improvements, or just say 'oh, hi Mark'.



## About the project
>**Rate Limiter** is a Java open source project focused on implementing rate-limiting mechanisms with clean code, good engineering practices, and extensible design.

The main goal of this repository is **not** to build the next production-standard rate limiter right away.  
The goal is to:

- explore rate-limiting algorithms deeply (Not only rate limiting, btw. A bunch of other topics are also explored)
- write high-quality Java code (Try it and you will love it)
- apply best practices and design patterns
- experiment with architecture and data structures
- build a small open source community around the project
- meet people, exchange ideas, and learn together

This repository is a place for **engineering practice**, **curiosity**, and **collaboration**.

---

## Motivation

Rate limiting is one of those topics that looks simple at first, but quickly becomes interesting when you start thinking about:

- algorithm trade-offs
- concurrency
- memory efficiency
- fairness
- registry lifecycle
- observability and metrics
- configuration management
- annotation-based APIs
- integration with modern Java features

This project exists to explore those topics in practice.

---

## Project philosophy

This project is built around a few simple ideas:

- **Code quality matters**
- **Learning by building matters**
- **Open source should be welcoming**
- **Small projects can still be well-designed**
- **Experimentation is valuable even without production pressure**

Even if this library never becomes production-ready, it can still become a useful and interesting codebase for learning, discussion, and contribution.

---

## Current status

At the moment, the project is centered around a **dynamic sliding window rate limiter** implementation.

Current direction:

- Java-first implementation
- focus on readability and extensibility
- simple core abstractions
- room for algorithmic experiments and refactoring

---

## Goals

### Technical goals

- implement multiple rate-limiting strategies
- improve configurability
- explore efficient data structures
- add lifecycle management for registries
- expose metrics and statistics
- design a clean annotation-based API
- keep the codebase understandable and contributor-friendly

### Community goals

- attract people interested in Java internals and backend infrastructure
- create a space for discussion and experimentation
- meet new people in the open source community
- collaborate on architecture, patterns, and implementation ideas

---
## Why this project may be interesting

This repository may be interesting if you want to:

- practice Java with a real infrastructure-style problem
- discuss concurrency and state management
- compare classic rate-limiting algorithms
- experiment with APIs and library design
- contribute to a small but thoughtful open source codebase

---

## Inspiration

This project is inspired by open source libraries such as **[Resilience4j](https://github.com/resilience4j/resilience4j)** and **[Micrometer](https://github.com/micrometer-metrics/micrometer)**.

They are much more mature, serious, and feature-complete projects, and this repository is **not trying to compete with them**.

Instead, this project is a smaller learning-oriented space inspired by the kind of engineering mindset those libraries represent:

- good design
- clear abstractions
- useful APIs
- practical infrastructure concerns
- thoughtful open source development

---

## Contributing

Contributions, ideas, refactorings, discussions, and feedback are welcome.

You can contribute through:

- proposing new algorithms
- discussing trade-offs between implementations
- improving API design
- improving JavaDoc and documentation
- suggesting better data structures
- helping with tests and benchmarks
- improving observability and metrics support

If you are interested in Java, backend infrastructure, concurrency, library design, or open source collaboration, you are very welcome here.

---

## Roadmap snapshot

- [x] Initial dynamic sliding window implementation
- [ ] Add more limiter algorithms
- [ ] Introduce annotation-based API
- [ ] Add registry cleanup with scheduler
- [ ] Explore Virtual Threads integration
- [ ] Support external configuration
- [ ] Replace or experiment with ring buffer storage (No idea where it and why do I need it, but okay)
- [ ] Add statistics and Prometheus-oriented metrics
- [ ] Improve tests, benchmarks, and documentation

---

## Project status

This is an **active learning and experimentation project**.

It should currently be viewed as:

- educational
- exploratory
- architecture-oriented
- open for discussion and contribution

Not production-first.  
Quality-first.

---

## License

Choose the license you prefer for the repository, for example:

- MIT
- Apache-2.0

## Bye!

<p align="center">
  <img src="public/touch_grass.gif" alt="Touch grass" />
</p>

What else do you want, candy?

There will be no candy.
