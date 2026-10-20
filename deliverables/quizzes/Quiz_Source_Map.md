# Quiz source map

Primary source: Craig Larman, *Applying UML and Patterns An Introduction to Object Oriented Analysis and Design and Iterative Development*, third edition, supplied as `references/Applying UML and Patterns 3rd.pdf`.

**Page convention:** all page numbers below are one-based PDF viewer pages, not inferred printed-book pages. The supplied file has 960 PDF pages. Its text was extracted end to end and indexed page by page in `docs/evidence/pdf-page-inventory.csv`; the answers were checked against the complete relevant source pages and their diagrams/tables, rather than an automatic summary. Source text and source figure renders are private working files in ignored `tmp/` and are not redistributed with the project.

## Quiz 1

| Question | PDF pages | Source sections and support |
|---|---|---|
| 1 | 41 | 1.3 What is Analysis and Design distinguishes investigation of the problem from a conceptual solution and its implementation. |
| 2 | 47–49 | 1.6 What is the UML explains the visual language, uses, perspectives and limits of notation alone. |
| 3 | 55, 58–61 | Chapter 2 introduction and 2.2 describe executable increments, feedback and evolutionary refinement. |
| 4 | 61 | 2.2 How Long Should an Iteration Be explains the textbook recommendation of two to six weeks and fixed iteration deadlines. This is the quiz answer; the project assignment separately permits three-day or one-week sprints. |
| 5 | 59–64 | 2.2 and 2.3 explain feedback on requirements, design, estimates and feature priorities in changing systems. |
| 6 | 72–73, 338, 340 | 2.7 and 14.1 describe modeling for understanding, simple tools, collaborative sketches and parallel models. 14.3 limits up-front modeling effort. |
| 7 | 219, 221–229 | 9.1–9.4 define conceptual classes, attributes and associations; explain a visual dictionary and the lower representational gap. Figures 9.2–9.6 distinguish conceptual models from software designs. |
| 8 | 317–318 | 13.2 and 13.3 define logical architecture, layers, strict/relaxed layering and deployment independence. |
| 9 | 322–325 | 13.6 gives the essential layering guidelines, separation of concerns and benefits. Figure 13.4 presents common information-system layers. |
| 10 | 329–330 | 13.7 defines both parts of Model-View Separation, benefits and the observer-interface qualification. |
| 11 | 341–342 | 14.4 defines dynamic interaction models and static class definitions and their complementary development. |
| 12 | 348–350 | 15.1 compares sequence and communication diagrams, including the strengths/weaknesses table and Figures 15.1–15.4. |
| 13 | 544–547 | 21.1 explains test-first rhythm, interface clarification, repeatability, confidence in changes and programmer motivation. |
| 14 | 424–426, 436–438 | 17.8 and 17.10 state Creator criteria, prefer a containing/aggregating class, and illustrate Sale creating SalesLineItems. |
| 15 | 438, 606–607 | 17.10 contraindications and 26.4 explain the Simple/Concrete Factory as a Pure Fabrication for complex creation and separation of concerns. Figure 26.5 illustrates service adapter creation. The answer concerns the textbook Factory pattern, not an assumption that every factory is the GoF Factory Method pattern. |

## Quiz 2

| Question | PDF pages | Source sections and support |
|---|---|---|
| 1 | 42–46 | 1.4 and 1.5 distinguish conceptual domain analysis, software-object design, implementation and corresponding models. |
| 2 | 47–49 | 1.6 lists UML as sketch, blueprint and programming language. The three modeling perspectives on page 49 are a different classification. |
| 3 | 58–61 | 2.2 lists iterative development benefits, including early risk mitigation, feedback, visible progress and process learning. Research claims are attributed to the textbook; no new independent statistics are asserted. |
| 4 | 62–64 | 2.3 criticizes the stability/predictability assumption behind waterfall and explains the need for feedback and adaptation. |
| 5 | 65–68, 74–76 | 2.4 gives an evolving analysis/design example; 2.5 explains risk/value-driven planning; 2.8 and 2.9 describe an agile UP and continuous verification. Percentages and calendar lengths in the example are illustrative, not a universal recipe. |
| 6 | 149–154 | 6.14 and 6.15 describe system boundary, primary actors, actor goals, event analysis, actor-goal lists and naming. The CRUD grouping exception appears on page 154. The original question has no question mark and is preserved. |
| 7 | 229–233, 239–241 | 9.4, 9.5, 9.10 and 9.12 explain model reuse, category lists, noun phrases, domain vocabulary and concepts versus attributes. The original question spelling `domainl` is preserved; its intended concept is domain model. |
| 8 | 242–244 | 9.13 explains description classes, the stock-depletion example, redundancy and retaining information independently of instances. Figure 9.9 shows one description for many items. |
| 9 | 273–280 | 10.1–10.6 define black-box SSDs, external events and their relationship to use cases, operation contracts and object design. Figure 10.2 shows a cash Process Sale scenario. |
| 10 | 317, 319–320 | 13.2 and 13.4 cover logical versus deployment architecture and significant structural and behavioral decisions. |
| 11 | 420–422, 612 | 17.6 defines named problem/solution knowledge, forces/trade-offs and communication benefits. 26.6 illustrates concise communication using pattern names. |
| 12 | 513–518 | 19.1 and 19.2 define visibility and attribute, parameter, local and global forms, with Figures 19.1–19.5. The four forms concern object references, not simply language access modifiers. |
| 13 | 427, 439–443 | 17.8 and 17.11 define Information Expert and collaborative partial experts; Figures 17.14–17.17 cover totals/subtotals. The contraindications warn against mixing persistence duties into a domain class. |
| 14 | 429–433, 448–458 | 17.13 describes facade and use-case/session controllers, delegation and avoiding bloated controllers. Page 453 explicitly distinguishes a web MVC controller from a GRASP controller. |
| 15 | 601–603, 612 | 26.1 defines Adapter and illustrates stable internal interfaces to varying external service APIs. 26.2 relates it to indirection, polymorphism and Protected Variations. |

## Source gaps and verification notes

No unresolved source gap was identified for the thirty questions: all requested concepts are supported by the supplied textbook. No supplementary general-knowledge explanation is needed to complete these answers. The original wording and numbering are preserved in both Word files, including the source typo noted above. NuistID, SEUID and Name are retained and populated from page 1 of the supplied previous report, as explicitly directed by the user during this session. Only identity information is reused; prior project dates, progress, Git commands and results are not reused as POS evidence.

The textbook discusses collaborative practices. The separate POS project is an individual project, as requested; quiz explanations of general methods do not assert that collaborators worked on this project. No quotations or invented printed page references have been added.

Source quality note: Figure 17.17 on PDF page 441 appears as an unavailable image in the supplied file. The surrounding explanation on pages 439–442 and Figure 17.16 explicitly support the Information Expert answer, so this image defect does not leave an unresolved answer gap.
