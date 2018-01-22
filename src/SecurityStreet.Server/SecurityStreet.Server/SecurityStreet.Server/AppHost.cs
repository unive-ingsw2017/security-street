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
        public AppHost()
            : base("SecurityStreet.Server", typeof(AutoveloxService).Assembly) { }

        /// <summary>
        /// Application specific configuration
        /// This method should initialize any IoC resources utilized by your web service classes.
        /// </summary>
        public override void Configure(Container container)
        {

            // TODO: Pick configuration from envirnoment
            //container.Register<IDbConnectionFactory>(new OrmLiteConnectionFactory("Data Source=VAIO-W10;Initial Catalog=Training;Integrated Security=True", SqlServerDialect.Provider));
            container.Register<IDbConnectionFactory>(new OrmLiteConnectionFactory("Server=tcp:unive-development.database.windows.net,1433;Initial Catalog=unive-development-swe-2018;Persist Security Info=False;User ID=devTeamUnive;Password=Development13@unive!;MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30;", SqlServerDialect.Provider));
            //container.Register<IDbConnectionFactory>(new OrmLiteConnectionFactory(":memory:", SqliteDialect.Provider));

            SetConfig(new HostConfig
            {
                DebugMode = true,
                HandlerFactoryPath = "api"
            });

            // Configure Servistack.Text for serialization
            JsConfig.EmitCamelCaseNames = true;
            JsConfig.IncludeNullValues = true;

            Plugins.Add(new AutoQueryFeature { MaxLimit = 1000 });

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
            }
        }
    }
}