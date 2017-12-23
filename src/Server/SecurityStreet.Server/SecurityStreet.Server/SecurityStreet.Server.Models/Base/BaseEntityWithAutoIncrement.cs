using ServiceStack.DataAnnotations;

namespace SecurityStreet.Server.Models.Base
{
    public class BaseEntityWithAutoIncrement
    {
        /// <summary>
        /// Gets or sets the identifier.
        /// </summary>
        /// <value>
        /// The identifier.
        /// </value>
        [PrimaryKey]
        [AutoIncrement]
        public int Id { get; set; }
    }
}
