using Funq;
using ServiceStack;
using SecurityStreet.Server.Services.Autovelox;
using ServiceStack.Data;
using ServiceStack.OrmLite;
using SecurityStreet.Server.Models.Entities;
using ServiceStack.Text;

namespace SecurityStreet.Server
{
    //VS.NET Template Info: https://servicestack.net/vs-templates/EmptyAspNet
    public class AppHost : AppHostBase
    {
        /// <summary>
        /// Base constructor requires a Name and Assembly where web service implementation is located
        /// </summary>
        public AppHost() : base("SecurityStreet.Server", typeof(AutoveloxService).Assembly) { }

        /// <summary>
        /// Application specific configuration
        /// This method should initialize any IoC resources utilized by your web service classes.
        /// </summary>
        public override void Configure(Container container)
        {
            // Pick connection string from web.config appsettings
            container.Register<IDbConnectionFactory>(new OrmLiteConnectionFactory(AppSettings.Get<string>("connectionstring"), SqlServerDialect.Provider));

            // Set host config
            SetConfig(new HostConfig
            {
                DebugMode = true,
                HandlerFactoryPath = "api"
            });

            // Configure Servistack.Text for serialization
            JsConfig.EmitCamelCaseNames = true;
            JsConfig.IncludeNullValues = true;

            /// Add autoquery
            Plugins.Add(new AutoQueryFeature { MaxLimit = 1000 });

            // Check table initialization
            InitTables(container);
        }

        /// <summary>
        /// Initializes the tables.
        /// </summary>
        /// <param name="container">The container.</param>
        private void InitTables(Container container)
        {
            using (var db = container.Resolve<IDbConnectionFactory>().Open())
            {
                db.CreateTableIfNotExists<Autovelox>();
                db.CreateTableIfNotExists<Crash>();
                db.CreateTableIfNotExists<NotificationSubscription>();
            }
        }
    }
}