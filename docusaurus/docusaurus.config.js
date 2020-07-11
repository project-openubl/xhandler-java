module.exports = {
  title: "XSender",
  tagline: "Java library for sending XML files through SOAP - SUNAT",
  url: "https://project-openubl.github.io/xsender-docs/",
  baseUrl: "/xsender-docs/",
  favicon: "img/favicon.ico",
  organizationName: "project-openubl", // Usually your GitHub org/user name.
  projectName: "xsender-docs", // Usually your repo name.
  themeConfig: {
    navbar: {
      title: "",
      logo: {
        alt: "XSender Server logo",
        src: "img/logo.svg",
      },
      links: [
        {
          to: "docs/",
          activeBasePath: "docs",
          label: "Docs",
          position: "left",
        },
        // { to: "blog", label: "Blog", position: "left" },
        {
          href: "https://github.com/project-openubl/xsender",
          label: "GitHub",
          position: "right",
        },
      ],
    },
    footer: {
      style: "dark",
      links: [
        {
          title: "Docs",
          items: [
            {
              label: "Introduction",
              to: "docs/",
            },
            {
              label: "Getting started",
              to: "docs/example/",
            },
          ],
        },
        {
          title: "Community",
          items: [
            {
              label: "Twitter",
              href: "https://twitter.com/openubl",
            },
          ],
        },
        {
          title: "More",
          items: [
            {
              label: "GitHub",
              href: "https://github.com/project-openubl/xsender",
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Project OpenUBL, Inc.`,
    },
    googleAnalytics: {
      trackingID: "UA-152706512-2",
    },
    gtag: {
      trackingID: 'UA-152706512-2',
    },
  },
  stylesheets: [
    "https://fonts.googleapis.com/css2?family=Red+Hat+Text&display=swap",
    "https://fonts.googleapis.com/css2?family=Red+Hat+Text:wght@400;700&display=swap",
  ],
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          // It is recommended to set document id as docs home page (`docs/` path).
          homePageId: "introduction",
          sidebarPath: require.resolve("./sidebars.js"),
          // Please change this to your repo.
          editUrl:
            "https://github.com/project-openubl/xsender/edit/master/docusaurus/",
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          editUrl:
            "https://github.com/project-openubl/xsender/edit/master/docusaurus/blog/",
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      },
    ],
  ],
  plugins: [
    "@docusaurus/plugin-google-analytics",
    "@docusaurus/plugin-google-gtag",
    [
      "@docusaurus/plugin-sitemap",
      {
        cacheTime: 600 * 1000, // 600 sec - cache purge period
        changefreq: "weekly",
        priority: 0.5,
      },
    ],
  ],
};
