using SecurityStreet.Server.Models.Base;
using ServiceStack.DataAnnotations;

namespace SecurityStreet.Server.Models.Entities
{
    [Alias("Autovelox")]
    public class Autovelox : BaseEntityWithAutoIncrement
    {
        /// <summary>
        /// Gets or sets the name.
        /// </summary>
        /// <value>
        /// The name.
        /// </value>
        public string Name { get; set; }
    }
}
